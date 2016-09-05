(ns clojure-applied.ch2.custom-collection
  (:import (clojure.lang Seqable Counted Indexed ILookup)
           (java.io Writer)))

;;; Represents tuple of size 2
;;; Supports following functions;
;;; - seq
;;; - count
;;; - nth
;;; - get
(deftype Pair [a b]
  Seqable
  (seq [_] (seq [a b]))

  Counted
  (count [_] 2)

  Indexed
  (nth [_ i]
    (case i
      0 a
      1 b
      (throw (IllegalArgumentException.))))
  (nth [this i _] (nth this i))

  ILookup
  (valAt [_ k _]
    (case k
      0 a
      1 b
      (throw (IllegalArgumentException.))))
  (valAt [this k] (.valAt this k nil))
  )

(def p (->Pair :a :b))

(counted? p)
(sequential? p)
(sorted? p)
(reversible? p)
(associative? p)

(seq p)
(count p)
(nth p 0)
(nth p 1)
#_(nth p 2)
(nth p 1 :x)
(get p 1)

;; ugly toString
(print p) ;=> #object[clojure_applied.ch2.custom_collection.Pair 0xb34e791


(defmethod print-method Pair
  [pair ^Writer w]
  (.write w (str "#" (.getName Pair)))
  (print-method (vec (seq pair)) w))

(defmethod print-dup Pair
  [pair w]
  (print-method pair w))


(print p) ;=> #clojure_applied.ch2.custom_collection.Pair[:a :b]
;; following syntax is used by reader to construct Java object: #class[args]
(def p2 #clojure_applied.ch2.custom_collection.Pair[:a :b])
(get p2 0)

