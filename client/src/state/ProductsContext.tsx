import React, {
  createContext,
  useState,
  useEffect,
  FunctionComponent
} from 'react'

export enum ProductEligibilityStatus {
  SELLER_NOT_ENROLLED,
  PRICE_DOES_NOT_MEET_THRESHOLD,
  PRODUCT_CATEGORY_EXCLUDED
}

export interface Product {
  id?: string
  title: string
  seller: string
  category: string
  price: number
  isEligible: boolean
  eligibilityStatus: string
}

export interface ProductContextValue {
  products: Product[]
  categories: string[]
  inFlight: boolean
  addToHistory: (product: Product) => void
}

export const ProductsContext = createContext<ProductContextValue>({
  products: [],
  categories: [],
  inFlight: false,
  addToHistory: () => null
})

export const ProductStateProvider: FunctionComponent = ({ children }) => {
  const [inFlight, setInFlight] = useState<boolean>(true)
  const [products, setProducts] = useState<Product[]>([])
  const [categories, setCategories] = useState<string[]>([])

  // inittial product fect
  useEffect(() => {
    const load = async () => {
      const products = await fetch(
        'http://localhost:8080/products/',
        {
          headers: {
            'content-type': 'application/json'
          }
        }
      )
      setProducts(await products.json())
      const categories = await fetch(
        'http://localhost:8080/categories/',
        {
          headers: {
            'content-type': 'application/json'
          }
        }
      )
      setCategories(await categories.json())
      setInFlight(false)
    }
    load()
  }, [])

  const addToHistory = (product: Product) => {
    setProducts([...products, product])
  }

  return <ProductsContext.Provider
    value={{
      products,
      inFlight,
      categories,
      addToHistory
    }}
    children={children}
  />
}
