
import request from 'supertest'
import { app } from '../src/app'

test('it serves healthcheck', async () => {
  const { body } = await request(app)
    .get('/')
    .expect(200)
  expect(body.healthy).toBeTruthy()
})

