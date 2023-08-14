import React from "react";

interface ProductCardProps {
  imgUrl: string;
  title: string;
  description: string;
  priceAmount: string;
  priceCurrency: string;
}

export default function ProductCard({
  description,
  imgUrl,
  title,
  priceAmount,
  priceCurrency,
}: ProductCardProps) {
  return (
    <div>
      <div className="card w-96 bg-base-100 shadow-xl">
        <figure>
          <img src={imgUrl} alt={title} />
        </figure>
        <div className="card-body">
          <h2 className="card-title">{title}!</h2>
          <p>{description}</p>
          <div className="card-actions justify-evenly items-center">
            <p className="text-xl text-accent">
              {priceCurrency} {priceAmount}
            </p>
            <button className="btn btn-primary">Buy Now</button>
          </div>
        </div>
      </div>
    </div>
  );
}
