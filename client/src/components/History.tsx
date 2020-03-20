import React, { FunctionComponent, useContext } from 'react'
import { Spin } from 'antd'
import { CheckCircleTwoTone, CloseCircleOutlined } from '@ant-design/icons'
import { ProductsContext, Product } from '../state/ProductsContext'

export const History: FunctionComponent = () => {
  const { products, inFlight } = useContext(ProductsContext)

  if (inFlight) {
    return <Spin />
  }

  return (
    <div className="history-list">
      <h2>History</h2>
      {
        products.map((product: Product) =>
          <div key={product.id} className="history-item">
            {product.isEligible &&
              <CheckCircleTwoTone
                twoToneColor="#52c41a"
                className="icon"
              />}
            {!product.isEligible &&
              <CloseCircleOutlined
                style={{ color: '#cc0000' }}
                className="icon"
              />
            }
            <h3>{product.title}</h3>
          </div>
        )
      }
    </div>
  )
}