
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

export const enrollmentHandler = async (req: Request, resp: Response) => {
  log('POST request', req.body)

  // validate body, return bad req if doesnt validate
  const valid = validator(req.body)
  if (!valid) {
    return resp
      .status(400)
      .json(validator.errors)
  }

  // escape values
  const [sellerUsername, isEnrolled] = Object.values(req.body)
    .map(value => escape(value))

  // check for existing user
  log(`checking for existing user`, `SELECT * FROM enrollment WHERE sellerUsername = ${sellerUsername}`)
  const [existing] = await query<Enrollment[]>(
    `SELECT * FROM enrollment WHERE sellerUsername=${sellerUsername}`
  )

  if (existing) {
    log(`updating enrollment status for ${sellerUsername}`)
    await update(
      `UPDATE enrollment SET isEnrolled=${isEnrolled}
       WHERE sellerUsername=${sellerUsername}
      `
    )

  } else {
    log(`setting new enrollment status`)
    await query(
      `INSERT INTO enrollment (sellerUsername, isEnrolled)
       VALUES (${sellerUsername}, ${isEnrolled})
      `
    )
  }

  resp.status(200).json({
    sellerUsername,
    isEnrolled
  })

}
