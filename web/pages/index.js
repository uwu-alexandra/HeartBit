// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase, ref } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
import { getAuth, signInWithEmailAndPassword, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";

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

const auth = getAuth();

var signinForm = document.querySelector('#login');
signinForm.addEventListener('click', (e) => {
  var email = document.getElementById('email').value;
  var password = document.getElementById('password').value;
  e.preventDefault();
  console.log('aici');
  signInWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      // Signed in 
      const user = userCredential.user;
      alert('Logged in successfully');
      console.log('ceva');
      // ...
    })
    .catch((error) => {
      const errorCode = error.code;
      const errorMessage = error.message;
      alert(errorMessage);
      console.log('Not good');
    });
});

onAuthStateChanged(auth, (user) => {
  if (user) {
    // User is signed in, see docs for a list of available properties
    const uid = user.uid;
    var email = user.email;
    console.log(email);
    var userType = 'unknown'; // Replace this with your code to determine the user's type
    /*switch (userType) {
      case 'b':
        document.location = 'WelcomeAdmin.html';
        break;
      case 'c':
        document.location = 'WelcomeMedic.html';
        break;
      case 'd':
        document.location = 'WelcomePacient.html';
        break;
    }*/
    // ...
  } else {
    //
    console.log('Not signed in')
}
});