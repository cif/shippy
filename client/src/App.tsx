import React, { FunctionComponent } from 'react'
import { History } from './components/History'
import { ProductForm } from './components/ProductForm'
import { ConfigurationForm } from './components/ConfigurationForm'

const App: FunctionComponent = () =>
  (
    <div className="app">
      <header>
        <span className="logo">shippy</span>
      </header>
      <section className="main">
        <section className="form">
          <ProductForm />
          <ConfigurationForm />
        </section>
        <section className="history">
          <History />
        </section>
      </section>
    </div>
  )


export default App;
