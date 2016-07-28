(ns clojure-applied.ch2.catalog-import)

;;; Using mutations for bulk import to improve efficiency of data structures

(def data
  (map #(str "item " %) (range 1 1000000)))

;; following is not-so-efficient version using typical immutable data structures and operations
(defn import-catalog [data]
  (reduce #(conj %1 %2) [] data))

(do (time
      (import-catalog data))
    nil)


;; more efficient version using transient collection
(defn import-catalog-fast [data]
  (persistent!
    (reduce #(conj! %1 %2) (transient []) data)))

(do (time
      (import-catalog-fast data))
    nil)


