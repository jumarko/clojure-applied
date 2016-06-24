(ns clojure-applied.ch1.apollo)

;;; Optional arguments in any position
;;; Consider extending space simulation to also include data about Apollo missions
;;; These missions varied (manned, had a lunar module, and so on)

(defn make-mission
  [name system launched manned? opts]
  (let [{:keys [cm-name ;; command module
                lm-name ;; lunar module
                orbits
                evas]} opts]
    ,,,))

(def apollo-4 (make-mission "Apollo 4"
                            "Saturn V"
                            #inst "1967-11-09T12:00:01-00:00"
                            false
                            {:orbits 3}))

;; set of default values can be provided by merging with map of defaults
(def mission-defaults {:orbits 0 :evas 0})
(defn make-mission
  [name system launched manned? opts]
  (let [{:keys [cm-name ;; command module
                lm-name ;; lunar module
                orbits
                evas]} (merge mission-defaults opts)]
    ,,,))


;;; Another common way to accept optional arguments is to descruture the varargs sequens as a map
;;; Notice that extra "&"
(defn make-mission
  [name system launched manned? & opts]
  (let [{:keys [cm-name
                lm-name
                orbits
                evas]} opts]
    ,,,))

(def apollo-4 (make-mission "Apollo 4"
                            "Saturn V"
                            #inst "1967-11-09T12:00:01-00:00"
                            false
                            :orbits 3))

(def apollo-11 (make-mission "Apollo 11"
                            "Saturn V"
                            #inst "1969-07-16T13:32:00-00:00"
                            true
                            :cm-name "Columbia"
                            :lm-name "Eagle"
                            :orbits 30
                            :evas 1))

;; In this case, default values can be provided by using :or destructuring
(defn make-mission
  [name system launched manned? & opts]
  (let [{:keys [cm-name
                lm-name
                orbits
                evas]
         :or {orbits 0 evas 0}} opts]
    ,,,))

(def apollo-4 (make-mission "Apollo 4"
                            "Saturn V"
                            #inst "1967-11-09T12:00:01-00:00"
                            false
                            :orbits 3))


;;; Constructors can also include calculations required to create an entity from derived values
;;; Let's extend the Planet definition with orbital eccentricity
;;; The eccentricity will be computed from eccentricity vector received as input.
(defn euclidean-norm [ecc-vector] ,,,)

(defrecord Planet [name moons volume mass aphelion perihelion orbital-eccentricity])

(defn make-planet
  "Make a planet from field values and an eccentricity vector"
  [name moons volume mass aphelion perihelion ecc-vector]
  (->Planet
   name moons volume mass aphelion perihelion
   (euclidean-norm ecc-vector)))
