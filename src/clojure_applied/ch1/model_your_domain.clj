(ns clojure-applied.ch1.model-your-domain)

;;; Modeling your entities
;;; Maps vs. Records
;;; Most of the time Records are bettern choice for domin entities
;;;

;; Modeling as map - the simplest form
(def earth {:name "Earth"
            :moons 1
            :volume 1.08321e12 ;; km^3
            :mass 5.97219e24 ;; kg
            :aphelion 152098232 ;; km, farthest from sun
            :perihelion 147098290 ;; km, closest to sun
            :type :Planet ;; entity type - often useful to drive dynamic behavior
            })

;; alternative defrecord - named type
(defrecord Planet [name
                   moons
                   volume
                   mass
                   aphelion
                   perihelion])

;; positional factory funciton
(->Planet "Earth" 1 1.08321e12 5.97219e24 152098232 147098290)

;; map factory function
(map->Planet {:name "Earth"
              :moons 1
              :volume 1.08321e12
              :mass 5.97219e24
              :aphelion 152098232
              :perihelion 147098290})
