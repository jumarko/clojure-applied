(ns clojure-applied.ch2.search)

;;; Indexed access - maps & vectors are two indexed collections provided by Clojure
(def earth {:name "Earth" :moons 1})

(get earth :name)
(earth :name)
;; for entities, invoking keyword  as a function is the preferred style for maps and records
(:name earth)

(def orders ["order-1" "order-2" "order-3"])
(get orders 0)
(orders 0)

;; if it's unclear what's happening => use get
(defn opposite-colors
  "Compute an opposite-color mapping for a given palette."
  [palette]
  ,,,)

;; ok, but confusing
(def palette {})
((opposite-colors palette) :magenta)
;; better
(get (opposite-colors palette) :magenta)


;;; Sequential Search

;; maps has constant-time access to their elements
;; similarly, sets support contains? function
(contains? #{1 3 4} 3)
;; but contains? doesn't work for vectors or lists
;; vector -> it's treated as index
(contains? [1 3 4] 3)
;; list -> IllegalArgumentException
(contains? '(1 3 4) 3)

;; => use some function
(some #{:oz} [:lb :oz :kg])
(some #{:oz} '(:lb :oz :kg))

;; however, some breaks when searching for nil or false values
(some #{nil} [:lb nil :kg])
(some #{nil} [:lb :kg])

;; relatively efficient implement of linear search - exits early (reduced)
(defn contains-val?
  [coll val]
  (reduce
    (fn [ret elem]
      (if (= val elem) (reduced true) ret))
    false
    coll))

(contains-val? [:lb :oz :kg] :oz)
(contains-val? [:lb nil :kg] nil)
(contains-val? [:lb :kg] nil)



