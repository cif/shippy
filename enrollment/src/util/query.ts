
import mysql from 'mysql'
const conn = mysql.createConnection({
  host: process.env.MYSQL_HOST,
  user: 'shippy',
  password: 'getitdone',
  database: 'shippy'
})

export const query = <T>(queryString: string) => new Promise<T[]>((resolve, reject) => {
  conn.query(queryString, (err, results) => {
    if (err) {
      reject(err)
    }
    const typedResults = []
    Object.keys(results).forEach((key) => {
      typedResults.push(results[key])
    })
    resolve(typedResults)
  })
})

export const update = <T>(queryString: string) => new Promise<T>((resolve, reject) => {
  conn.query(queryString, (err, results) => {
    if (err) {
      reject(err)
    }
    resolve(results)
  })
})
