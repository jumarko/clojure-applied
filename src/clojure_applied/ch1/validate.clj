(ns clojure-applied.ch1.validate
  (:require [schema.core :as s]))

;;; Let's begin with traditional approach when defining records
;;; -> no schema checking, just some comments
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


;;; Now, let's use Prismatic Schema to describe our recipes instead.

(s/defrecord Ingredient
    [name :- s/Str
     quantity :- s/Int
     unit :- s/Keyword])

(s/defrecord Recipe
    [name :- s/Str
     description :- s/Str
     ingredients :- [Ingredient]
     steps :- [s/Str]
     servings :- s/Int])

;; ask for explanation of Schema
(print (s/explain Recipe))

;; we can validate our data against schema
(s/check Recipe spaghetti-tacos)

(def bad-spaghetti-tacos
  (map->Recipe
   {:name "Spaghetti tacos"
    :description "It's spaghetti... in a taco."
    ;; keyword is used instead of string for Ingredient name
    :ingredients [(->Ingredient :Spaghetti 1 :lb)
                  (->Ingredient "Spaghettisauce" 16 :oz)
                  (->Ingredient "Taco shell" 12 :shell)]
    :steps ["Cook spaghetti according to box."
            "Heat spaghetti sauce until warm."
            "Mix spaghetti and sauce."
            "Put spaghetti in taco shells and serve."]
    :servings 4
    }))

;; explicit schema validation
;; (print  (s/check Recipe bad-spaghetti-tacos))


;;; Schema also has own version of defn
(s/defn add-ingredients :- Recipe
  [recipe :- Recipe & ingredients :- [Ingredient]]
  (update-in recipe [:ingredients] into ingredients))

;;; automatic validation can be turned on using with-fn-validation
(s/with-fn-validation
  (add-ingredients
   ;; uncomment to try schema validation
   ;;   bad-spaghetti-tacos
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
    })
   (->Ingredient "oil" 4 :oz)))
