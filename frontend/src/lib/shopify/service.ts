import { CreateCheckoutData, ProductsData } from ".";

export class ShopifyService {
  private shopName: string;
  private accessToken: string;
  private baseUrl: string;

  constructor() {
    this.shopName = import.meta.env.VITE_SHOP_NAME;
    this.accessToken = import.meta.env.VITE_ACCESS_TOKEN;
    this.baseUrl = `https://${this.shopName}.myshopify.com/api/2023-07/graphql.json`;
  }

  private async executeGql<T>(
    query: string,
    variables: object | undefined = undefined
  ): Promise<T> {
    const response = await fetch(this.baseUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-Shopify-Storefront-Access-Token": this.accessToken,
      },
      body: JSON.stringify({ query, variables }),
    });

    if (!response.ok) {
      throw new Error("Failed to call graphql");
    }

    const json = await response.json();

    console.trace(`GraphQL Result`, json);

    return json;
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

    return this.executeGql(query);
  }

  async createCheckout(
    variantId: string,
    quantity: number
  ): Promise<CreateCheckoutData> {
    const mutation = `
    mutation ($input: CheckoutCreateInput!) {
      checkoutCreate(input: $input) {
        checkout {
          id
          webUrl
        }
      }
    }
    `;
    const variables = {
      input: {
        lineItems: [
          {
            variantId,
            quantity,
          },
        ],
      },
    };
    return this.executeGql(mutation, variables);
  }
}

export const shopifyService = new ShopifyService();
