
import { query, update } from './query'

describe('query util', () => {
  test('throws when queries go bad', async () => {
    try {
      await query<boolean>('bad query qyntasxses')
      expect(true).toBe(false) // unreachable
    } catch (e) {
      expect(e).toBeTruthy()
    }
  })

  test('throws when queries go bad', async () => {
    try {
      await update<boolean>('bad query qyntasxses')
      expect(true).toBe(false) // unreachable
    } catch (e) {
      expect(e).toBeTruthy()
    }
  })
})
