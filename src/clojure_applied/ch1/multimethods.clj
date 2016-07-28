(ns clojure-applied.ch1.multimethods
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
(defrecord Store [,,,])

(defn cost-of [store ingredient] ,,,)

(defmulti cost (fn [entity store] (class entity)))

(defmethod cost Recipe [recipe store]
  (reduce m/+$ m/zero-dollars
          (map #(cost % store) (:ingredients recipe))))

(defmethod cost Ingredient [ingredient store]
  (cost-of store ingredient))
