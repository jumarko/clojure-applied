(ns clojure-applied.ch3.orbital
  (:require [clojure-applied.ch1.model-your-domain :as m])
  (:import (clojure_applied.ch1.model_your_domain Planet)))

;;; Our goal is to extract planets' orbital periods
;;; The function is as follows
;;;     T = 2 * PI * sqrt(a^3 / mu); where mu = G * M
;;;   G is gravitational constant, M is a mass of central star

;; check https://en.wikipedia.org/wiki/Gravitational_constant
(def G "Gravitational constant. Units m^3 kg^-1 s^-2" 6.674e-20)

(defn semi-major-axis
  "The planet's average distance from the star" [p]
  (/ (+ (:aphelion p) (:perihelion p))
     2))

(defn mu [mass] (* G mass))

(def planet (planets 0))
(def central-star-mass (:mass sun))
(defn orbital-period
  "The time it takes for a planet to make a complete orbit around a central start of given mass, in seconds."
  [planet central-star-mass]
  (* Math/PI 2
     (Math/sqrt (/ (Math/pow (semi-major-axis planet) 3)
                   (mu central-star-mass)))))

;; we must now have wrap functional orbital-period which accepts 2 arguments into
;; a proper transformation function which accepts single argument )

(defn orbital-periods
  "Given a collection of planets, and a star, return the orbital periods of every planet."
  [planets star]
  (let [solar-mass (:mass star)]
    (map (fn [planet] (orbital-period planet solar-mass ))
         planets)))

(def planets [(m/map->Planet {:name "Earth"
                                  :moons 1
                                  :volume 1.08321e12
                                  :mass 5.97219e24
                                  :aphelion 152098232
                                  :perihelion 147098290})
                  ;; check https://en.wikipedia.org/wiki/Mars
                  (m/map->Planet {:name "Mars"
                                  :moons 2
                                  :volume 1.6318e11
                                  :mass 6.4171e23
                                  :aphelion 249200000
                                  :perihelion 206700000})
                  ;; check https://en.wikipedia.org/wiki/Jupiter
                  (m/map->Planet {:name "Jupiter"
                                  :moons 67
                                  :volume 1.4313e15
                                  :mass 1.8986e27
                                  :aphelion 816040000
                                  :perihelion 740550000})

                  ])

;; check https://en.wikipedia.org/wiki/Sun
(def sun {:mass 1.98855e30})

(orbital-periods planets sun)



;;; Transducers
;;; Notice the difference between "sequence" and "into"
;;; - sequence produces lazy sequence (sequences caches the values that have been computed)
;;; - into eagerly computes the entire output (often more efficient in both memory and time)

;; to create a map transducer, omit the input collection on the call to map
(defn orbital-period-transformation
  "Create a map transformation for planet->orbital-period"
  [star]
  (map #(orbital-period % (:mass star))))

;; previous transformation can be used with a variety of input sources and output conditions
;; following is the same as "classic" map scenario
(defn orbital-periods-trans [planets star]
  (sequence (orbital-period-transformation star) planets))
(orbital-periods-trans planets sun)

;; or we can use following function for returning vector as a result
(defn orbital-periods-trans-vector [planets star]
  (into [] (orbital-period-transformation star) planets))
(orbital-periods-trans-vector planets sun)
;; or produce a list
(defn orbital-periods-trans-list [planets star]
  (into () (orbital-period-transformation star) planets))
(orbital-periods-trans-list planets sun)



;;; Reducing to a Value - p. 49

;; we often combine reduce with applying a transformation
(defn total-moons [planets]
  (reduce + 0 (map :moons planets)))
(total-moons planets)

;; alternatively, you can use transduce
(defn total-moons [planets]
  (transduce (map :moons) + 0 planets))
(total-moons planets)

;; Early termination - reduced
(defn find-planet [planets pname]
  (reduce
    (fn [_ planet]
      (when (= pname (:name planet))
        (reduced planet)))
    nil
    planets))
(find-planet planets "Earth")


;;; Filtering and Removing Values - p.50

;; filtering only planet instances
(defn planet? [entity]
  (instance? Planet entity))

(defn total-moons [entities]
  (reduce + 0
          (map :moons
               (filter planet? entities))))
(total-moons planets)

;; the same rewritten with thread-last macro
(defn total-moons [entities]
  (->> entities
       (filter planet?)
       (map :moons)
       (reduce + 0)))
(total-moons planets)

;; when using transducers to compose functions
;; we need to use function composition
(def moons-transform
  (comp (filter planet?) (map :moons)))
(defn total-moons [entities]
  (transduce moons-transform + 0 entities))
(total-moons planets)

((comp #(str % " a dalej") println) "ahoj")



;;; Take and Drop - p. 52
(defn nth-page
  "Return up to page-size results for the nth (0-based) page of source."
  [source page-size page]
  (->> source
       (drop (* page page-size))
       (take page-size)))
(nth-page (range 100) 10 3)

;; transducer form would use reduced to signal early termination and avoid realizing results
;; beyond the requested ranges

;; sometimes you want both the page and the rest of collection for further processing
(defn page-and-rest
  [source page-size]
  (split-at page-size source))

;; check also take-while, drop-while, and split-with functions



;;; Sorting and Duplicate Removal

;; sorting uses either default or provided comparator
(take 5 (sort (map :name planets)))
(take 5 (sort-by :name planets))

(defn smallest-n
  [planets n]
  (take n
        (sort-by :mass planets)))
(smallest-n planets 2)



;;; Grouping Values - p. 54
(defn index-planets [planets]
  (group-by #(first (:name %)) planets))
(index-planets planets)

;; common example of group-by predicate is a predicate that returns boolean - true or false
(defn has-moons? [planet]
  (pos? (:moons planet)))
(defn split-moons
  [planets]
  (group-by has-moons? planets))
(split-moons planets)
