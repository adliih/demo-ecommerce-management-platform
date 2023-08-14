import React, { useEffect, useState } from "react";
import ProductCard from "./ProductCard";
import { ProductsData, ShopifyService } from "../lib/shopify";

const shopifyService = new ShopifyService();

function ProductList() {
  const [data, setData] = useState<ProductsData>();

  useEffect(() => {
    shopifyService.fetchProducts().then(setData);
  }, []);

  if (!data) {
    return (
      <div className="flex h-screen items-center justify-center">
        <span className="loading loading-bars loading-lg"></span>
      </div>
    );
  }

  return (
    <div className="flex flex-wrap py-4 gap-4 justify-center">
      {data?.data.products.edges.map(({ node }) => {
        const variant = node.variants.edges[0];
        return (
          <ProductCard
            key={node.id}
            description={node.description}
            imgUrl={
              node.images.edges.length ? node.images.edges[0].node.url : ""
            }
            title={node.title}
            priceAmount={variant.node.price.amount}
            priceCurrency={variant.node.price.currencyCode}
          />
        );
      })}
    </div>
  );
}

export default ProductList;
