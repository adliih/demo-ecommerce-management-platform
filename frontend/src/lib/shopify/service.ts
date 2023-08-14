import { ProductsData } from ".";

export class ShopifyService {
  private shopName: string;
  private accessToken: string;
  private baseUrl: string;

  constructor() {
    this.shopName = import.meta.env.VITE_SHOP_NAME;
    this.accessToken = import.meta.env.VITE_ACCESS_TOKEN;
    this.baseUrl = `https://${this.shopName}.myshopify.com/api/2023-07/graphql.json`;
  }

  async fetchProducts(): Promise<ProductsData> {
    const query = `
    {
      products(first: 10) {
        edges {
          node {
            id
            title
            description
            images (first: 1) {
              edges {
                node {
                  url
                }
              }
            }
            description
            variants(first: 1) {
              edges {
                node {
                  id
                  price {
                    amount
                    currencyCode
                  }
                }
              }
            }
          }
        }
      }
    }
    `;

    const response = await fetch(this.baseUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-Shopify-Storefront-Access-Token": this.accessToken,
      },
      body: JSON.stringify({ query }),
    });

    if (!response.ok) {
      throw new Error("Failed to fetch products");
    }

    const data = await response.json();
    console.trace(`Got products`, data);
    return data;
  }
}
