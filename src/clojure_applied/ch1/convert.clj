(ns clojure-applied.ch1.convert)

;;; Although type-based dispatch is the most common case,
;;; value-based dispatch is needed in plenty of cases -> that's where multimethods shine
;;;
;;; Consider new feature: Building a shopping list by adding together all the ingredients
;;; in one or more recipes.
;;; We might have some recipes that specify spaghetti in pounds and some that specify it in ounces.
;;; We need to do conversions => multimethods give us the ability to provide conversions
;;; that depend on the source and target types


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


(defmulti convert
  "Convert quantity from unit1 to unit2, matching on [unit1 unit2]"
  (fn [unit1 unit2 quantity] [unit1 unit2]))

;; lb to oz
(defmethod convert [:lb :oz] [_ _ lb] (* lb 16))

;; oz to lb
(defmethod convert [:oz :lb] [_ _ oz] (/ oz 16))

;; default case - if no match found
(defmethod convert :default [u1 u2 q]
  (if (= u1 u2)
    q
    (assert false (str "Unknown unit conversion from " u1 " to " u2))))

(defn ingredient+
  "Add two ingredients into a single ingredient, combining their quantities with unit conversion if necessary."
  [{n1 :name q1 :quantity u1 :unit :as i1} {n2 :name q2 :quantity u2 :unit :as i2}]
  (assert (= n1 n2) (str "You can add different ingredients togeter: " n1 " <> " n2))
  (assoc i1 :quantity (+ q1 (convert u2 u1 q2))))

(ingredient+
 (->Ingredient "Spaghetti" 1/2 :lb)
 (->Ingredient "Spaghetti" 4 :oz))

;; Notice that original implementation in book allowed to add two different ingredients
#_(ingredient+
 (->Ingredient "Spaghetti" 1 :lb)
 (->Ingredient "Spaghettisauce" 16 :oz))
