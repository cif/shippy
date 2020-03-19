
import { logger } from './util/logger'
import { query } from './util/query'

const log = logger('app:bootstrap')

export const bootstrap = async () => {
  try {
    log(`trying to bootstap mysql table`)
    await query(`
    CREATE TABLE IF NOT EXISTS enrollment (
       id int(11) unsigned NOT NULL AUTO_INCREMENT,
       sellerUsername varchar(255) CHARACTER SET utf8 DEFAULT NULL,
       isEnrolled tinyint(1) DEFAULT 0,
       PRIMARY KEY (id),
       UNIQUE KEY sellerUsername (sellerUsername)
     ) ;
   `)
  } catch (e) {
    log(`failed to bootstap mysql table`)
  }

  try {
    log(`trying to bootstap test records`)
    // create a record for the java app integration tests (even if reduntant)
    await query(`INSERT INTO enrollment (sellerUsername, isEnrolled) VALUES ('spring.test.seller', 1)`)
    await query(`INSERT INTO enrollment (sellerUsername, isEnrolled) VALUES ('spring.test.seller.unenrolled', 0)`)
  } catch (e) {
    log(`the data boostrap failed on start. this may be okay.`)
  }
}
