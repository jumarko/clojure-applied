(ns clojure-applied.ch1.protocols
  (:require [clojure-applied.ch1.money :as m]))

;;; We want to invoke the same generic domain operation "How much does it cost?"
;;; on entities of two specific types: Recipe and Ingredient

(defrecord Recipe
    [name ;; string
     description ;; string
     ingredients ;; sequence of Ingredient
     steps ;; sequence of string
     servings ;; number of servings
     ])

(defrecord Ingredient
    [name ;; string
     quantity ;; amount
     unit ;; keyword
     ])

;; particular instance may look like this:
(def spaghetti-tacos
  (map->Recipe
   {:name "Spaghetti tacos"
    :description "It's spaghetti... in a taco."
    :ingredients [(->Ingredient "Spaghetti" 1 :lb)
                  (->Ingredient "Spaghettisauce" 16 :oz)
                  (->Ingredient "Taco shell" 12 :shell)]
    :steps ["Cook spaghetti according to box."
            "Heat spaghetti sauce until warm."
            "Mix spaghetti and sauce."
            "Put spaghetti in taco shells and serve."]
    :servings 4
    }))


;; at first we need to extend our domain model with store
;; to have a way how to look up the cost of an ingredient in a particular grocery store
(defrecord Store [name
                  prices
                  tax-rate])

(def my-store (->Store "Best of best"
                       {"Spaghetti" (m/map->Money {:amount 3 :currency m/USD})
                        "Spaghettisauce" (m/map->Money {:amount 5 :currency m/USD})
                        "Taco shell" (m/map->Money {:amount 4 :currency m/USD})}
                       19/100))

(defn cost-of [store ingredient]
  (let [ingredient-unit-price ((:prices store) (:name ingredient))]
    ;; Note: to make things simpler we don't take different quantity units into account!
    (update-in ingredient-unit-price [:amount] * (:quantity ingredient))
    ))

;; (cost-of my-store (->Ingredient "Spaghetti" 10 :lb))


;; Now, define domain operations via protocols
(defprotocol Cost
  (cost [entity store]))

(extend-protocol Cost
  Recipe
  (cost [recipe store]
    (reduce m/+$ m/zero-dollars
            (map #(cost % store) (:ingredients recipe))))
  Ingredient
  (cost [ingredient store]
    (cost-of store ingredient)))

(cost spaghetti-tacos my-store)


;;; Extending Protocols to Protocols
;;; For example, you might need to extend the recipe manager further to calculate
;;; not only the cost of the items but also the cost of the items if bought from a particular store,
;;; including the location-specific taxes.

(defprotocol TaxedCost
  (taxed-cost [entity store]))

(defn tax-rate [store]
  (:tax-rate store))

;; we'd like to layer the TaxedCost protocol over existing Cost protocol, but this isn't allowed in Clojure:
#_(extend-protocol TaxedCost
  Cost
  (taxed-cost [entity store]
    (* (cost entity store) (+ 1 (tax-rate store)))))

;; However, we can provide the same effect at runtime
(extend-protocol TaxedCost
  Object
  (taxed-cost [entity store]
    (if (satisfies? Cost entity)
      (do (extend-protocol TaxedCost
            (class entity)
            (taxed-cost [entity store]
              (let [entity-cost (cost entity store)]
                (m/->Money {:amount (* (:amount  entity-cost) (+ 1 (tax-rate store)))}                             {:currency (:currenty entity-cost)}))
              ))
          (taxed-cost entity store))
      (assert false (str "Unhandled entity: " entity)))))

(taxed-cost spaghetti-tacos  my-store)
