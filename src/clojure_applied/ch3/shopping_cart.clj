(ns clojure-applied.ch3.shopping-cart
  "Shopping cart example from Chapter 3 page 55"
  (:require [clojure-applied.ch1.money :refer [make-money +$ *$ USD]]))


(defrecord CatalogItem [number department description price])
(defrecord Cart [number customer line-items settled?])
(defrecord LineItem [quantity catalog-item price])
(defrecord Customer [customer-name email membership-number])

(def my-carts [(map->Cart {:number     116,
                          :customer   (map->Customer {:customer-name     "Danny Tanner"
                                                      :email             "danny@fullhouse.example.com"
                                                      :membership-number 28374})
                          :line-items [(map->LineItem {:quantity     3
                                                       :catalog-item (map->CatalogItem {
                                                                                        :number      664
                                                                                        :department  :clothing
                                                                                        :description "polo shirt L"
                                                                                        :price       (make-money 2515 USD)})
                                                       :price        (make-money 7545 USD)})
                                       (map->LineItem {:quantity     1
                                                       :catalog-item (map->CatalogItem {
                                                                                        :number      621
                                                                                        :department  :clothing
                                                                                        :description "khaki pants"
                                                                                        :price       (make-money 3500 USD)})
                                                       :price        (make-money 3500 USD)})
                                       ]
                          :settled?   true}), ,,,])

(defn- line-summary
  "Given a LineItem with a CatelogItem, returns a map containing CatelogItem's :department
   and LineItem's :price"
  [line-item]
  {:department (get-in line-item [:catalog-item :department])
   :total (:price line-item)})

(defn- department-total [m k v]
  (assoc m k (reduce +$ (map :total v))))

(defn revenue-by-department [carts]
  (->> (filter :settled? carts)
       ;; we don't need only small portion of cart - line item containing catalog item
       (mapcat :line-items)
       (map line-summary)
       (group-by :department)
       (reduce-kv department-total {})
       ))

(revenue-by-department my-carts)


