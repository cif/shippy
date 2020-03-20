
import mysql from 'mysql'

export const conn = mysql.createConnection({
  host: process.env.MYSQL_HOST,
  user: 'shippy',
  password: 'getitdone',
  database: 'shippy'
})

export const query = <T>(queryString: string) =>
  new Promise<T[] | undefined>((resolve, reject) => {
    conn.query(queryString, (err, results) => {
      if (err) {
        // tslint:disable-next-line
        return reject(err)
      }
      const typedResults = []
      Object.keys(results).forEach((key) => {
        typedResults.push(results[key])
      })
      resolve(typedResults)
    })
  })

export const update = <T>(queryString: string) =>
  new Promise<T>((resolve, reject) => {
    conn.query(queryString, (err, results) => {
      if (err) {
        // tslint:disable-next-line
        return reject(err)
      }
      resolve(results)
    })
  })
