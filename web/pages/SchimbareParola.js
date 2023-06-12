function showAuthenticatedElements() {
  const elements = document.querySelectorAll('.authenticated');
  elements.forEach((element) => {
    element.style.display = 'block';
  });
}

// Helper function to hide authenticated elements
function hideAuthenticatedElements() {
  const elements = document.querySelectorAll('.authenticated');
  elements.forEach((element) => {
    element.style.display = 'none';
  });
}
hideAuthenticatedElements();
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase, ref, set } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
import { getAuth, updatePassword, onAuthStateChanged  } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";

// Your web app's Firebase configuration
const firebaseConfig = {
  // Your config details
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
onAuthStateChanged(auth, (user) => {
  if (user) {
    showAuthenticatedElements();
    uid = user.uid;
  } else {
    window.location.href = 'index.html'; // Redirect to index.html if no user is logged in
  }
});
// Get the UID from the URL parameter
const urlParams = new URLSearchParams(window.location.search);
const uid = urlParams.get('uid');

const form = document.querySelector('form');
const newPasswordInput = document.querySelector('.ei10_111_4_17');
const confirmPasswordInput = document.querySelector('.ei10_114_4_17');

console.log(auth.currentUser);
form.addEventListener('submit', (e) => {
  e.preventDefault();

  const newPassword = newPasswordInput.value;
  const confirmPassword = confirmPasswordInput.value;

  if (newPassword !== confirmPassword) {
    alert('Passwords do not match.');
    return;
  }
  updateCurrentUserPassword(newPassword);
  set(ref(database, `Users/${uid}/password`), newPassword)
    .then(() => {
      alert('Password updated successfully.');
      window.location.href = `WelcomePacient.html?uid=${uid}`;
      // Redirect to another page or perform any other actions
    })
    .catch((error) => {
      console.error(error);
      alert('An error occurred while updating the password.');
    });
});
const anulare = document.getElementById('anulare');
anulare.addEventListener('click', () => {
    window.location.href = `Profil.html?uid=${uid}`;
});


function updateCurrentUserPassword(newPassword) {
  const user = auth.currentUser;

  if (user) {
    updatePassword(user, newPassword)
      .then(() => {
        // Password updated successfully
        console.log('Password updated successfully');
      })
      .catch((error) => {
        // Handle password update errors
        console.error('Error updating password:', error);
      });
  } else {
    // User is not logged in
    console.error('No logged-in user found');
  }
}

// Usage example
const newPassword = 'newPassword123';


