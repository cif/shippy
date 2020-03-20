// adds a 100ms delay to fetching for better non blinky-ux

const sleep = (ms: number) => {
  return new Promise((resolve) => {
    setTimeout(resolve, ms);
  });
}

export const fetchy = async (url: string, options?: any) => {
  await sleep(100)
  return fetch(url, options)
}