export interface ProductVariant {
  node: {
    id: string;
    price: {
      amount: string;
      currencyCode: string;
    };
  };
}

export interface ProductImage {
  node: {
    url: string;
  };
}

export interface Product {
  node: {
    id: string;
    title: string;
    description: string;
    images: {
      edges: ProductImage[];
    };
    variants: {
      edges: ProductVariant[];
    };
  };
}

export interface ProductsData {
  data: {
    products: {
      edges: Product[];
    };
  };
}
