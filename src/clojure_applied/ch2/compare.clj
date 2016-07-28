(ns clojure-applied.ch2.compare)

;;; Sorted collections

;; bad sorting functions - remove values which are not equal
(defn compare-authors-badly [s1 s2]
  (compare (:lname s1) (:lname s2)))

(sorted-set-by compare-authors-badly
               {:fname "Jeff" :lname "Smith"}
               {:fname "Bill" :lname "Smith"})
;; => #{{:fname "Jeff", :lname "Smith"}}


;; better sorting
(defn compare-authors [s1 s2]
  (let [c (compare (:lname s1) (:lname s2))]
    (if (zero? c)
      (compare (:fname s1) (:fname s2))
      c)))

(sorted-set-by compare-authors
               {:fname "Jeff" :lname "Smith"}
               {:fname "Bill" :lname "Smith"})


;; using "juxt" idiom
(defn compare-author [s1 s2]
  (letfn [(project-author [author]
            ((juxt :lname :fname) author))]
    (compare (project-author s1) (project-author s2))))

(sorted-set-by compare-author
               {:fname "Jeff" :lname "Smith"}
               {:fname "Bill" :lname "Smith"})


