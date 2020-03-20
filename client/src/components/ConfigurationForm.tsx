import React, { FunctionComponent } from 'react'
import {
  Form,
  Input,
  Button,
  Select,
} from 'antd'
import { LoadingOutlined } from '@ant-design/icons'
import { useProductForm } from '../hooks/useProductForm'
import { useConfigurationForm } from '../hooks/useConfigurationForm'

export const ConfigurationForm: FunctionComponent = () => {
  const {
    inFlight,
    config = {},
    updateConfiguration
  } = useConfigurationForm()
  const {
    categories
  } = useProductForm()

  if (inFlight) {
    return (
      <div id="config">
        <h2>Loading configuration...</h2>
      </div>
    )
  }
  return (
    <Form
      name="config"
      layout="vertical"
      onFinish={updateConfiguration}
      initialValues={{ ...config }}
    >
      <h2>Manage Eligibility</h2>
      <Form.Item
        label="Minimum Price"
        name="minimumPrice"
        rules={[{ required: true, message: 'Required!' }]}
      >
        <Input autoComplete='off' style={{ width: 200 }} />
      </Form.Item>
      <Form.Item
        label="Categories"
        name="categories"
        rules={[{ required: true, message: 'At least one category is required' }]}
      >
        <Select mode="multiple" placeholder="Please select a category">
          {
            categories.map((category: string, index: number) =>
              <Select.Option value={category} key={category}>
                {category}
              </Select.Option>
            )
          }
        </Select>
      </Form.Item>
      <Button
        type="primary"
        htmlType="submit"
        disabled={inFlight}
      >
        {!inFlight && <span>Update Config</span>}
        {inFlight && <LoadingOutlined style={{ fontSize: 24 }} spin />}
      </Button>
    </Form>
  )
}