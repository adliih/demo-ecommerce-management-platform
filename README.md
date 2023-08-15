# Demo e-Commerce of Shopify x Java x React

This repo contains 3 seperate service

- Shopify Custom Applications (not being used now)
- Backend Service
- Custom Storefront Frontend

## Shopify Custom Applications

Folder: `shopify`

This is not being used for now, I'm still looking into what can I do with this.

## Backend Service

Folder: `backend`

This service is built using Java Spring boot.
Currently installed feature are:

- Shopify integration to import Shop, Product, and Variant into DB

## Custom Storefront Frontend

Folder: `frontend`

This service is built using Vite + React + Typescript
It currently has basic feature:

- Integration with Shopify API
- Product List
- Create a checkout of a single product

Yes, this frontend is not using any API from the backend service, we should probably migrate it later on.

# Todos

- Add stripe integration in Backend service
- Migrate all frontend api call to backend
- Checkout Backennd API to create order on shopify and create payment link on stripe to simulate Stripe x Shopify integration

# How to run

## Shopify Custom Applications (not being used now)

```
npm i --prefix shopify
npm run dev --prefix shopify
```

## Backend Service

copy and adjust application.properties, example can be found at [application.properties.example](backend/src/main/resources/application.properties.example)

```
docker-compose up -d db app
```

## Custom Storefront Frontend

copy and adjust .env, example can be found at [.env](frontend/.env)

```
npm i --prefix frontend
npm run dev --prefix frontend
```
