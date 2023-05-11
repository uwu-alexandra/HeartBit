
// Import the functions you need from the SDKs you need

//import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
import { getAuth, signOut, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";


// TODO: Add SDKs for Firebase products that you want to use

// https://firebase.google.com/docs/web/setup#available-libraries


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

const auth = getAuth();
var logout = document.querySelector('#logout');
onAuthStateChanged(auth, (user) => {
    if (user) {
      // User is signed in, see docs for a list of available properties
      const uid = user.uid;
      var email = user.email;
      console.log(email);
      var userType = user.email.toString().charAt(0);
      // ...
    } else {
      // User is signed out
      // ...
      console.log('Not signed in')
    }
  });
logout.addEventListener('click', (e) => {
    e.preventDefault();
    console.log('in eventlistener');
    signOut(auth).then(() => {
        alert('Signed out successfully');
        document.location = 'index.html';
    }).catch((error) => {
        console.log('Not logged in');
    });
});








