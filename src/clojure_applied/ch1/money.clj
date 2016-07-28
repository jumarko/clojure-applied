(ns clojure-applied.ch1.money)

;;; Models monetary values
;;; Inspired by Martin Fowler: Patterns of Enterprise Application Architecture.

(declare validate-same-currency)

(defrecord Currency [divisor sym desc])

(defrecord Money [amount ^Currency currency]
  java.lang.Comparable
  (compareTo [m1 m2]
    (validate-same-currency m1 m2)
    (compare (:amount m1) (:amount m2))))

(defn- validate-same-currency
  [m1 m2]
  (or (= (:currency m1) (:currency m2))
      (throw
       (ex-info "Currencies do not match."
                {:m1 m1 :m2 m2}))))

(def currencies {:usd (->Currency 100 "USD" "US Dollars")
                 :eur (->Currency 100 "EUR" "Euro")})

(def USD (:usd currencies))
(def EUR (:eur currencies))

;; example of compareTo
(.compareTo
 (map->Money {:amount 1200 :currency USD})
 (map->Money {:amount 1100 :currency USD}))


;;; functions for adding, comparing, multiplying, and other operations

(defn =$
  ([m1] true)
  ([m1 m2] (zero? (.compareTo m1 m2)))
  ([m1 m2 & monies]
   (every? zero? (map #(.compareTo m1 %) (conj monies m2)))))

;; Example of =$
(=$
 (map->Money {:amount 1200 :currency EUR})
 (map->Money {:amount 1200 :currency EUR})
 (map->Money {:amount 1200 :currency EUR}))


(defn +$
  ([m1] m1)
  ([m1 m2]
   (validate-same-currency m1 m2)
   (->Money (+ (:amount m1) (:amount m2)) (:currency m1)))
  ([m1 m2 & monies]
   (reduce +$ m1 (conj monies m2))))

;; Example of +$
(+$
 (map->Money {:amount 1 :currency USD})
 (map->Money {:amount 1.10 :currency USD})
 (map->Money {:amount 100 :currency USD}))


(defn *$ [m n] (->Money (* n (:amount m)) (:currency m)))

;; Example of *$
(*$
 (map->Money {:amount 50 :currency USD})
 10)



;;; Let's build a flexible Money constructor that includes default values
;;; values more likely to be needed are placed earlier in the argument list
(defn make-money
  ([] (make-money 0))
  ([amount] (make-money amount (:usd currencies)))
  ([amount currency] (->Money amount currency)))

(make-money)
(make-money 1)
(make-money (:eur currencies))

;; However, it's often useful to accept optional arguments in any order -> Map destructuring
;; Check apollo.clj for more examples


(def zero-dollars (->Money 0 USD))
