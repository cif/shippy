
import request from 'supertest'
import { app } from '../src/app'
import { conn } from '../src/util/query'

describe('enrollment', () => {
  const { floor, random } = Math
  const sellerUsername = `test.seller${floor(random() * 1000)}`

  afterAll((done) => {
    conn.end()
  })
  test('it enrolls a seller', async () => {
    const { body } = await request(app)
      .post('/enroll')
      .send({
        sellerUsername,
        isEnrolled: true
      })
      .expect(200)
    expect(body.isEnrolled).toBe(true)
  })

  test('validates enrollment correctly', async () => {
    await request(app)
      .post('/enroll')
      .send({
        sellerUsername,
        bad: 'data'
      })
      .expect(400)
  })

  test('it gets status', async () => {
    const { body } = await request(app)
      .get(`/enroll/status/${sellerUsername}`)
      .expect(200)
    expect(body.isEnrolled).toBe(true)
  })

  test('updates enrollment', async () => {
    const { body } = await request(app)
      .post('/enroll')
      .send({
        sellerUsername,
        isEnrolled: false
      })
      .expect(200)
    expect(body.isEnrolled).toBe(false)
  })

  test('it persisted status', async () => {
    const { body } = await request(app)
      .get(`/enroll/status/${sellerUsername}`)
      .expect(200)
    expect(body.isEnrolled).toBe(false)
  })

  test('it return false for not found record', async () => {
    const { body } = await request(app)
      .get(`/enroll/status/non-existing user`)
      .expect(200)
    expect(body.isEnrolled).toBe(false)
  })
})
