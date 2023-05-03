// Import the functions you need from the SDKs you need

//import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase, ref, set } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
// import { createUserWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";

import { getAuth, createUserWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";


  // Import the functions you need from the SDKs you need

  import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";

  // TODO: Add SDKs for Firebase products that you want to use

  // https://firebase.google.com/docs/web/setup#available-libraries


  // Your web app's Firebase configuration

  const firebaseConfig = {

    apiKey: "AIzaSyDHsp8a_NU7F8J-Gr5DaaWsoPUN79JO_ZY",

    authDomain: "proiectip-f2b57.firebaseapp.com",

    databaseURL: "https://proiectip-f2b57-default-rtdb.firebaseio.com",

    projectId: "proiectip-f2b57",

    storageBucket: "proiectip-f2b57.appspot.com",

    messagingSenderId: "134775428923",

    appId: "1:134775428923:web:a3542cd019c1e515c4891e",

    measurementId: "G-EGZ06QWFGX"

  };



  // Initialize Firebase

  const app = initializeApp(firebaseConfig);



// Initialize Firebase

const database = getDatabase(app);
const auth = getAuth();
const signupForm = document.querySelector('#signup-medic');

signupForm.addEventListener('submit', (e) => {
    e.preventDefault();

    var id = signupForm['id'].value;
    var email = signupForm['email'].value;
    var nume = signupForm['nume'].value;
    var prenume = signupForm['prenume'].value;
    //var adresa = signupForm['adresa'].value;
    var password = signupForm['password'].value;

    createUserWithEmailAndPassword(auth, email, password)
        .then((userCredential) => {
            //var dbRef = database.ref();
            const user = userCredential.user;
            // save additional user info to Firebase Realtime Database
            //dbRef.child('users/' + user.uid).set({
            set(ref(database, 'users/' + user.uid), {

                id,
                email,
                nume,
                prenume,
                //          adresa
            });
            document.location = 'WelcomeMedic.html';
        })
        .catch((error) => {
            const errorCode = error.code;
            const errorMessage = error.message;
            console.error(errorMessage);
            alert('An error has occurred while signing in. Please try again.');
        });
});