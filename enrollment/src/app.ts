require('dotenv').config({
  path: `${__dirname}/../.env`
})

import express, { Express, Response, NextFunction } from 'express'
import { getEnrollmentStatus, postEnrollment } from './handlers/enrollment'
import body from 'body-parser'
import async from 'express-async-handler'
import { logger } from './util/logger'

const log = logger('app:service')
export const app: Express = express()

// routes
app.use(body.json())
app.post('/enroll', async(postEnrollment))
app.get('/enroll/status/:sellerUsername', async(getEnrollmentStatus))

// healthcheck
app.get('/', (_: undefined, res: Response) => {
  res.json({
    healthy: true,
  })
  res.end()
})

// error handler
app.use((err: Error, req: Request, resp: Response, next: NextFunction) => {
  log(`application error`, err.stack)
  resp
    .status(500)
    .send('An application error occurred. Please try again later')
})
