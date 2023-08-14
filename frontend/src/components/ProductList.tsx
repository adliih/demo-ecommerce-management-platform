import React, { useEffect, useState } from "react";
import ProductCard from "./ProductCard";
import { ProductsData, ShopifyService } from "../lib/shopify";

const shopifyService = new ShopifyService();

function ProductList() {
  const [data, setData] = useState<ProductsData>();

  useEffect(() => {
    shopifyService.fetchProducts().then(setData);
  }, []);

  return (
    <div className="grid grid-cols-3 gap-4">
      {data?.data.products.edges.map(({ node }) => (
        <ProductCard
          key={node.id}
          description={node.description}
          imgUrl={node.images.edges.length ? node.images.edges[0].node.url : ""}
          title={node.title}
        />
      ))}
    </div>
  );
}

export default ProductList;
