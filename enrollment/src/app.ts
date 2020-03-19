require('dotenv').config({
  path: `${__dirname}/../.env`
})

import express, { Response } from 'express'
import { enrollmentHandler } from './handlers/enrollmentHandler'
import body from 'body-parser'
import async from 'express-async-handler'

export const app = express()

// routes
app.use(body.json())
app.post('/enroll', async(enrollmentHandler))

// healthcheck
app.get('/', (_: undefined, res: Response) => {
  res.json({
    healthy: true,
  })
  res.end()
})

// error handler
app.use((err: Error, req: Request, resp: Response) => {
  resp.status(500).send('An application error occurred.')
})
