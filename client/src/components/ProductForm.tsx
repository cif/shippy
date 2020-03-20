import React, { FunctionComponent } from 'react'
import {
  Form,
  Input,
  Button,
  Select,
  Spin
} from 'antd'
import { LoadingOutlined } from '@ant-design/icons'
import { useProductForm } from '../hooks/useProductForm'

export const ProductForm: FunctionComponent = () => {
  const {
    categories,
    inFlight,
    enrollmentStatusInFlight,
    handleSubmit,
    handleUserNameChange
  } = useProductForm()
  return (
    <Form
      name="product"
      layout="vertical"
      onFinish={handleSubmit}
    >
      <h2>Check Shipping Eligibility</h2>
      <Form.Item
        label="Title"
        name="title"
        rules={[{ required: true, message: 'Title is required!' }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Seller"
        name="seller"
        rules={[{ required: true, message: 'Seller is required!' }]}
      >
        <Input onChange={(e: any) => {
          e.persist()
          handleUserNameChange(e)
        }}
        />
      </Form.Item>
      <div>{enrollmentStatusInFlight && <LoadingOutlined style={{ fontSize: 24 }} spin />}</div>

      <Form.Item
        label="Category"
        name="category"
        rules={[{ required: true, message: 'Category is required!' }]}
      >
        <Select placeholder="Please select a category">
          {
            categories.map((category: string, index: number) =>
              <Select.Option value={index} key={category}>
                {category}
              </Select.Option>
            )
          }
        </Select>
      </Form.Item>

      <Form.Item
        label="Price"
        name="price"
        rules={[{ required: true, message: 'Seller is required!' }]}
      >
        <Input style={{ width: 200 }} />
      </Form.Item>

      <Button
        type="primary"
        htmlType="submit"
        disabled={inFlight}
      >
        {!inFlight && <span>Check it!</span>}
        {inFlight && <LoadingOutlined style={{ fontSize: 24 }} spin />}
      </Button>
    </Form>
  )
}