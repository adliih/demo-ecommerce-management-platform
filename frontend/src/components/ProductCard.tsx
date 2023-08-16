import { useState } from "react";
import { shopifyService } from "../lib/shopify";
import Loading from "./Loading";

interface ProductCardProps {
  imgUrl: string;
  title: string;
  description: string;
  priceAmount: string;
  priceCurrency: string;
  variantId: string;
}

export default function ProductCard({
  description,
  imgUrl,
  title,
  priceAmount,
  priceCurrency,
  variantId,
}: ProductCardProps) {
  const [isLoading, setIsLoading] = useState(false);

  const handleCheckout = async () => {
    setIsLoading(true);
    try {
      // TODO make quantity as parameter
      const quantity = 1;

      const checkoutData = await shopifyService.createCheckout(
        variantId,
        quantity
      );
      window.location.href = checkoutData.data.checkoutCreate.checkout.webUrl;
    } catch (error) {
      console.error("Error creating checkout", error);
      setIsLoading(false);
    }
  };

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
            {isLoading ? (
              <button className="btn btn-primary">
                <Loading />
              </button>
            ) : (
              <button className="btn btn-primary" onClick={handleCheckout}>
                Buy Now
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
