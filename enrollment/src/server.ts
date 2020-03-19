import { app } from './app'
import { logger } from './util/logger'
import { query } from './util/query'

const log = logger('app:service')

async function main() {
  // attempt to create mysql database and table
  if (process.env.NODE_ENV !== 'production') {
    await query(`
     CREATE TABLE IF NOT EXISTS enrollment (
        id int(11) unsigned NOT NULL AUTO_INCREMENT,
        sellerUsername varchar(255) CHARACTER SET utf8 DEFAULT NULL,
        isEnrolled tinyint(1) DEFAULT 0,
        PRIMARY KEY (id)
      ) ;
    `)
  }

  // Start the server
  const port = process.env.PORT || 3001
  app.listen(port as number, '0.0.0.0', () => {
    log(`Service started on ${port}`)
  })
}

main()
  .catch(e => log('error starting app', e))

