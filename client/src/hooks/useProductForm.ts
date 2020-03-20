import { useContext, useState } from 'react'
import { ProductsContext, Product } from '../state/ProductsContext'
import { notification } from 'antd'
import debounce from 'debounce'

export type MapLike = {
  [key: string]: any
}

export interface ProductFormHook {
  categories: string[]
  inFlight: boolean
  enrollmentStatusInFlight: boolean
  handleSubmit: (data: MapLike) => void
  handleUserNameChange: (e: any) => void
}

export const useProductForm = (): ProductFormHook => {
  const [submitInFlight, setSubmitInFlight] = useState<boolean>(false)
  const [enrollmentStatusInFlight, setEnrollmentStatusInFlight] = useState<boolean>(false)
  const {
    categories,
    addToHistory,
    inFlight: productsOrCategoriesInFlight
  } = useContext(ProductsContext)

  const handleSubmit = async (data: any) => {
    setSubmitInFlight(true)
    try {
      // submit the product to the service
      const productResponse = await fetch('http://localhost:8080/products/', {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
          'content-type': 'application/json'
        },
      })
      const product: Product = await productResponse.json()

      // display the notification
      if (product.isEligible) {
        notification['success']({
          message: 'The Radness',
          description:
            'This product is eligible for free shipping',
        })
      } else {
        notification['error']({
          message: 'The Sadness',
          description:
            `This product is NOT eligible for free shipping. ${product.eligibilityStatus}`,
        })
      }

      // add to the product history state
      addToHistory(product)
      setSubmitInFlight(false)
    } catch (e) {
      notification['error']({
        message: 'The Sadness',
        description:
          `Network error. I dunno.`,
      })
    }
  }

  const handleUserNameChange = debounce(async (e: any) => {
    console.log(e.target.value);
    setEnrollmentStatusInFlight(true)
    const resp = await fetch('http://localhost:3001/enroll/status/')
    console.log(await resp.json())
    setEnrollmentStatusInFlight(false)
  }, 500)


  return {
    categories,
    inFlight: productsOrCategoriesInFlight || submitInFlight,
    enrollmentStatusInFlight,
    handleSubmit,
    handleUserNameChange
  }
}