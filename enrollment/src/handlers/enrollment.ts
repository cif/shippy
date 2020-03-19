
import { Request, Response } from 'express'
import { escape } from 'mysql'
import Ajv from 'ajv'
import { query, update } from '../util/query'
import { logger } from '../util/logger'

const schema = require('../schema/enrollment.post.schema.json')
const log = logger('app:handler:enrollment')
const ajv = new Ajv()
const validator = ajv.compile(schema)

export interface Enrollment {
  sellerUsername: string
  isEnrolled: boolean
}

export const postEnrollment = async (req: Request, resp: Response) => {
  log('POST request', req.body)

  // validate body, return bad req if doesnt validate
  const valid = validator(req.body)
  if (!valid) {
    return resp
      .status(400)
      .json(validator.errors)
  }

  // escape values
  const [safeUsername, safeEnrolled] = Object.values(req.body)
    .map(value => escape(value))

  // check for existing user
  log(`checking for existing user`)
  const [existing] = await query<Enrollment>(
    `SELECT * FROM enrollment WHERE sellerUsername=${safeUsername}`
  )

  if (existing) {
    log(`user found, updating enrollment status for ${safeUsername}`)
    await update(
      `UPDATE enrollment SET isEnrolled=${safeEnrolled}
       WHERE sellerUsername=${safeUsername}
      `
    )

  } else {
    log(`setting new enrollment status for ${safeUsername}`)
    await query(
      `INSERT INTO enrollment (sellerUsername, isEnrolled)
       VALUES (${safeUsername}, ${safeEnrolled})
      `
    )
  }

  log(`successfully completed enrollment`)
  resp
    .status(200)
    .json(req.body)
}

export const getEnrollmentStatus = async (req: Request, resp: Response) => {
  const { sellerUsername } = req.params
  log(`checking enrollment status for ${sellerUsername}`)

  const [existing] = await query<Enrollment>(
    `SELECT * FROM enrollment WHERE sellerUsername=${escape(sellerUsername)}`
  )

  if (!existing) {
    log(`${sellerUsername} is not enrolled`)
    resp.json({
      isEnrolled: false,
    })
    return
  }

  // cheap js bool casting
  const isEnrolled = !!existing.isEnrolled
  log(`${sellerUsername} status enrolled=${isEnrolled}`)
  resp.json({
    isEnrolled,
  })
}
