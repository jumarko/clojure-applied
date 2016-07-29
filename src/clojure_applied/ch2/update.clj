(ns clojure-applied.ch2.update
  (:require [medley.core :as m]))

;;; Updating maps

(def earth {:name "Earth"
            :moons 1
            :volume 1.08321e12                              ;; km^3
            :mass 6.97219e24                                ;; kg
            :aphelion 152098232                             ;; farthest from sun
            :perihelion 147098290                                    ;; closes to sun
            })

(update earth :moons inc)
;; notice that update also works with non-existent keys if update function accepts nil
(update earth :moons2 (fn [x] 10))


;; updating many values at once
;; suppose we have keys in wrong format (strings instead of keywords)
(def earth {"name" "Earth"
            "moons" 1
            "volume" 1.08321e12                              ;; km^3
            "mass" 6.97219e24                                ;; kg
            "aphelion" 152098232                             ;; farthest from sun
            "perihelion" 147098290                                    ;; closes to sun
            })
(m/map-keys keyword earth)

;; vals can be updated as well
(m/map-vals #(into [] [%]) earth)


;; other utility functions
(m/filter-keys #(clojure.string/starts-with? % "m") earth)
(m/filter-vals #(if (number? %) (> % 1000000)) earth)