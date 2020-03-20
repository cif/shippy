import React, { FunctionComponent } from 'react'
import {
  Form,
  Input,
  Button,
  Select,
  Tooltip
} from 'antd'
import { LoadingOutlined, CheckCircleTwoTone } from '@ant-design/icons'
import { useProductForm } from '../hooks/useProductForm'

export const ProductForm: FunctionComponent = () => {
  const {
    categories,
    inFlight,
    isEnrolled,
    enrollmentStatusInFlight,
    handleSubmit,
    handleUserNameChange,
    handleInlineEnrollment
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
        <Input autoComplete='off' />
      </Form.Item>

      <Form.Item
        label="Seller"
        name="seller"
        rules={[{ required: true, message: 'Seller is required!' }]}

      >
        <Input
          autoComplete='off'
          onChange={(e: any) => {
            e.persist()
            handleUserNameChange(e)
          }}
        />
      </Form.Item>
      <div>{enrollmentStatusInFlight && <LoadingOutlined style={{ fontSize: 24 }} spin />}</div>
      {isEnrolled !== null &&
        <div>
          {isEnrolled &&
            <div>
              <CheckCircleTwoTone
                twoToneColor="#52c41a"
                className="icon"
              /> <h4>Nice! This seller is enrolled in the program.</h4>
            </div>
          }
          {!isEnrolled &&
            <div>
              <h4>That user is not enrolled yet...</h4>
              <Button
                type="default"
                onClick={handleInlineEnrollment}
              >
                Enroll Now
            </Button>
            </div>
          }
        </div>
      }
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
        <Input autoComplete='off' style={{ width: 200 }} />
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