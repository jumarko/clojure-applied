(ns clojure-applied.ch1.recipe)

;;;; 3 ways how to model relationships
;;;; 1. nesting
;;;; 2. identifiers
;;;; 3. stateful references


;;; 1. nesting
(defrecord Recipe
    [name ;; string
     author ;; recipe creator
     description ;; string
     ingredients ;; list of ingredients
     steps ;; sequence of string
     servings ;; number of servings
     ]
  )

(defrecord Person
    [fname ;; first name
     lname ;; last name
     ])

;; now consider options that we have for connecting Recipe and Person

;; If we are interested in making the Recipe the centerpiece of our application
;; and consider authors to be merely descriptive informationabout the recipe,
;; we can nest the person underneath the recipe:


;; Another version of our application might consider the authors to be of more importance.
;; Users might expect that updating a person's information in one recipe
;; will update that information in all the recipes.
;; In that case, we might want to model the Person as the primary entity
;; and have the person nest a list of Recipes he authored.
(defrecord Recipe
    [name ;; string
     description ;; string
     ingredients ;; list of ingredients
     steps ;; sequence of string
     servings ;; number of servings
     ])
(defrecord Person
    [fname
     lname
     recipes ;; list of recipes
     ])

(def alex
  (->Person
   "Alex"
   "Miller"
   ;; nested recipes
   [(->Recipe
     "Toast"
     "Crispy bread"
     ["Slice of bread"]
     ["Toast bread in toaster"]
     1)
    (->Recipe
     "French Fries"
     "Very special french fries"
     ["potatoes" "oil"]
     ["Cut potatoes" "Fry potatoes"]
     1)]))


;;; 2. identifiers
;;; You might want both Person and Recipe to be top-level entities that can each be updated
;;; in a single place.
;;; For example, a Recipe might have multiple authors
(defrecord Recipe
    [name ;; string
     author ;; recipe creator ID
     description ;; string
     ingredients ;; list of ingredients
     steps ;; sequence of string
     servings ;; number of servings
     ]
  )

(defrecord Person
    [fname ;; first name
     lname ;; last name
     ])

(def people
  {"p1" (->Person "Alex" "Miller")})

(def recipes
  {"r1" (->Recipe
         "Toast"
         "p1" ;; Person id
         "Crispy bread"
         ["Slice of bread"]
         ["Toast bread in toaster"]
         1)})


;;; 3. statful reference
;;; Use when you want to refer to another entity and allow that relationship
;;; to change over time.
;;; Explained in Chapter 4.
