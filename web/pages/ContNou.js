// Import the functions you need from the SDKs you need

//import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase, ref, set } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
// import { createUserWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";

import { getAuth, createUserWithEmailAndPassword, setPersistence, browserLocalPersistence } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";


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
const database = getDatabase(app);
const auth = getAuth();
const signupForm = document.querySelector('#signup-medic');

signupForm.addEventListener('submit', (e) => {
  e.preventDefault();
  persistence: 'session'
  var id = signupForm['id'].value;
  var email = signupForm['email'].value;
  var nume = signupForm['nume'].value;
  var prenume = signupForm['prenume'].value;
  var password = signupForm['password'].value;
  var cnp = signupForm['cnp'].value;
  createUserWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      const user = userCredential.user;
      let userType = '';

      if (id.startsWith('1')) {
        userType = 'medic';
      } else if (id.startsWith('2')) {
        userType = 'pacient';
      } else if (id.startsWith('3')) {
        userType = 'admin';
      } else {
        userType = 'unknown';
      }
      if(userType === 'pacient')
      {
        var ekg_low = 60;
        var ekg_high = 100;
        
        var puls_low = 60;
        var puls_high = 100;

        var temp_low = 36.1;
        var temp_high = 37.2;
        
        var umid_low = 30;
        var umid_high = 50;
        
        set(ref(database, 'Users/' + user.uid), {
          id,
          email,
          nume,
          prenume,
          userType,
          password,
          cnp,
          ekg_low,
          ekg_high,
          puls_low,
          puls_high,
          temp_low,
          temp_high,
          umid_low,
          umid_high,
        })
        .then(() => {
          document.location = 'WelcomePacient.html?uid=' + user.uid;
        })
        .catch((error) => {
          console.error(error);
          alert('An error occurred while adding user data to the database.');
        });
      }
      else{
        set(ref(database, 'Users/' + user.uid), {
          id,
          email,
          nume,
          prenume,
          userType,
          password,
          cnp,
        })
        .then(() => {
          switch (userType) {
            case 'medic':
              document.location = 'WelcomeMedic.html?uid=' + user.uid;
              break;
            case 'admin':
              document.location = 'Admin.html';
              break;
            default:
              alert('Unknown user type. Please contact the administrator.');
              break;
          }
        })
        .catch((error) => {
          console.error(error);
          alert('An error occurred while adding user data to the database.');
        });
      }
    })
    .catch((error) => {
      const errorCode = error.code;
      const errorMessage = error.message;
      console.error(errorMessage);
      alert('An error has occurred while signing in. Please try again.');
    });
});