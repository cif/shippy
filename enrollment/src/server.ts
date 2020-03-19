import { app } from './app'
import { bootstrap } from './bootstrap'
import { logger } from './util/logger'

const log = logger('app:service-statup')

async function main() {
  log(`starting service...`)
  // attempt to create mysql database and table
  if (process.env.NODE_ENV !== 'production') {
    // FIXME: this should be some kind of migration library/fully baked solution
    // but is at the same time a great minimalist demonstration.
    await bootstrap()
  }

  // Start the server
  const port = process.env.PORT || 3001
  app.listen(port as number, '0.0.0.0', () => {
    log(`started on ${port}`)
  })
}

main()
  .catch(e => log('error starting app', e))

