import { useState, useEffect } from 'react'
import { notification } from 'antd'
import { MapLike } from './useProductForm'
import { fetchy } from '../fetchy'

export interface ShippingConfiguration {
  minimumPrice: number
  categories: number[]
}

export interface ConfigurationForm {
  inFlight: boolean
  config: ShippingConfiguration | null
  updateConfiguration: (data: MapLike) => void
}

export const useConfigurationForm = (): ConfigurationForm => {
  const [inFlight, setInFlight] = useState<boolean>(true)
  const [config, setConfig] = useState<ShippingConfiguration | null>(null)

  const updateConfiguration = async (data: MapLike) => {
    setInFlight(true)
    try {
      const resp = await fetchy('http://localhost:8080/products/config/', {
        method: 'PUT',
        body: JSON.stringify(data),
        headers: {
          'content-type': 'application/json'
        }
      })
      setConfig(await resp.json())
      notification['success']({
        message: 'The Radness',
        description:
          'The configuration has been updated.',
      })
      setInFlight(false)
    } catch (e) {
      notification['success']({
        message: 'The Sadness',
        description:
          'The update failed. Check console for details',
      })
      console.error(e)
    }
  }

  useEffect(() => {
    // load the existing configuration
    const load = async () => {
      try {
        const resp = await fetchy('http://localhost:8080/products/config/')
        setConfig(await resp.json())
        setInFlight(false)
      } catch (e) {
        console.error('Network problems getting the eligibility configuation', e)
      }
    }
    load()
  }, [])

  return {
    config,
    inFlight,
    updateConfiguration
  }
}