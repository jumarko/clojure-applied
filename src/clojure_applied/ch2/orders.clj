(ns clojure-applied.ch2.orders)

;;; Imaging the FIFO-like data structure that we need to tracking the orders
;;; - remove order from the beginning
;;; - add orders to the end
;;; notice that neither vector nor list are efficient for this case

(defn cook [order]
  (println "Cooking: " order))

;; using vector
(defn new-orders [] [])

;; efficient adding to the end of vector
(defn add-order [orders order]
  (conj orders order))

;; inefficient creation of (rest orders) if we want to retain vector data structure
(defn cook-order [orders]
  (cook (first orders) )
  (rest orders))


;; neither list is suitable...
(defn new-orders [] '())

(defn add-order [orders order]
  ;; list has no function for adding to add -> we need to concatanate two lists => O(n)
  (concat orders (list order)))


;; The best data structure for this use case is Queue
;; Unfortunately, Clojure doesn't have a literal for queue
(defn new-orders [] clojure.lang.PersistentQueue/EMPTY)

(defn add-order [orders order]
  (conj orders order))

(defn cook-order [orders]
  ;; notice that peek is for queue and list same as first
  ;; - for the vector it returns the last element (and is much more efficient than "last")
  (cook (peek orders))
  (pop orders))


;; Demo
(def orders (-> (new-orders)
                (add-order {:food "chicken" :table 9})
                (add-order {:food "potatoe" :table 1})
                (add-order {:food "mushrooms" :table 99})))

(cook-order orders)

