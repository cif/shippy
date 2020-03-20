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
  isEnrolled: boolean | null
  enrollmentStatusInFlight: boolean
  handleSubmit: (data: MapLike) => Promise<void>
  handleUserNameChange: (e: any) => void
  handleInlineEnrollment: (e: any) => Promise<void>
}

export const useProductForm = (): ProductFormHook => {
  const [submitInFlight, setSubmitInFlight] = useState<boolean>(false)
  const [isEnrolled, setIsEnrolled] = useState<boolean | null>(null)
  const [sellerName, setSellerName] = useState<string>('')
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
    const username = e.target.value
    setSellerName(username)
    if (e === '') {
      setIsEnrolled(null)
      setEnrollmentStatusInFlight(false)
    }
    setEnrollmentStatusInFlight(true)
    const resp = await fetch(`http://localhost:3001/enroll/status/${username}`)
    const json = await resp.json()
    setIsEnrolled(json.isEnrolled)
    setEnrollmentStatusInFlight(false)
  }, 400)

  const handleInlineEnrollment = async () => {
    setIsEnrolled(null)
    setEnrollmentStatusInFlight(true)
    try {
      await fetch(`http://localhost:3001/enroll`, {
        method: 'POST',
        body: JSON.stringify({
          sellerUsername: sellerName,
          isEnrolled: true
        }),
        headers: {
          'content-type': 'application/json'
        }
      })
      setEnrollmentStatusInFlight(false)
      setIsEnrolled(true)
    } catch (e) {
      console.error(e)
      setEnrollmentStatusInFlight(true)
      setIsEnrolled(null)
    }
  }

  return {
    categories,
    inFlight: productsOrCategoriesInFlight || submitInFlight,
    isEnrolled,
    enrollmentStatusInFlight,
    handleSubmit,
    handleUserNameChange,
    handleInlineEnrollment
  }
}