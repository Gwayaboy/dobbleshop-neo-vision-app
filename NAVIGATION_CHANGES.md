# Navigation Changes - Animal Management Update

## Summary
Removed the dedicated "Animaux" tab from bottom navigation and integrated animal selection into the Dashboard screen via a dialog, matching the reference implementation at the mock app.

## Changes Made

### 1. **Bottom Navigation Simplified** ✅
**File:** `AppDestination.kt`
- Removed `Pets` destination from `bottomNavDestinations` list
- Navigation now has only 4 tabs:
  - 🏠 **Accueil** (Home/Dashboard)
  - 🍽️ **Repas** (Meals/Feeding)
  - 📹 **Caméra** (Camera)
  - ⚙️ **Réglages** (Settings)

### 2. **Navigation UI Updated** ✅
**File:** `DobbleShopApp.kt`
- Removed `Pets` icon and label from NavigationBar
- Updated icon mapping to only include 4 destinations
- Updated label mapping to only include 4 destinations

### 3. **Navigation Graph Simplified** ✅
**File:** `AppNavHost.kt`
- Removed `PetsScreen` composable route
- Removed `PetsScreen` import
- Simplified `DashboardScreen` call (removed navigation callbacks)

### 4. **Dashboard Enhanced** ✅
**File:** `DashboardScreen.kt`

#### New Features:
- **Animal Selection Dialog:** Full-screen overlay showing all pets
- **Active Pet Display:** Shows selected pet's details on dashboard
- **ViewModel Integration:** Connected to `PetsViewModel` for real data

#### New Components:
1. **ActiveAnimalCard** (Enhanced)
   - Shows pet avatar (🐱/🐶)
   - Displays name, breed
   - Shows daily food/water rations (🥣 65g/j, 💧 220ml/j)
   - "Changer" button opens selection dialog
   - Vivid blue colors matching theme

2. **AnimalSelectionDialog** (New)
   - Full-screen modal dialog (90% height)
   - Header with "Animaux" title and close button
   - Handles all `PetsUiState` cases:
     - **Loading:** Shows CircularProgressIndicator
     - **Empty:** Shows "Aucun animal" message with add button
     - **Success:** Shows scrollable list of pets
     - **Error:** Shows error message
   - "+ Ajouter un animal" button at bottom

3. **AnimalSelectionCard** (New)
   - Large pet cards with avatar
   - Shows name, breed, age
   - "Actif" badge for currently selected pet
   - Pet details grid showing:
     - Weight (kg)
     - Age (years + months)
     - Daily food portion (🥣)
     - Daily water portion (💧)
   - "Détails" and "Sélectionner" buttons for inactive pets
   - Light blue background for active pet
   - Clickable cards (except for active pet)

#### Visual Design:
- Light gray background (#F5F5F5)
- White cards with rounded corners (12dp)
- Blue accent color (#2196F3)
- Green "Actif" badge (#4CAF50)
- Circular avatars with light blue backgrounds
- Proper spacing and elevation

### 5. **Imports Updated** ✅
Added necessary imports:
- `androidx.hilt.navigation.compose.hiltViewModel`
- `androidx.compose.foundation.clickable`
- `androidx.compose.foundation.lazy.LazyColumn`
- `androidx.compose.foundation.lazy.items`
- `androidx.compose.foundation.shape.CircleShape`
- `androidx.compose.ui.window.Dialog`
- `com.dobbleshop.neovision.data.model.Pet`
- `com.dobbleshop.neovision.ui.viewmodel.PetsViewModel`
- `com.dobbleshop.neovision.ui.viewmodel.PetsUiState`

## User Flow

### Before (with Animaux tab):
1. User taps "Animaux" tab in bottom navigation
2. Navigates to separate Pets screen
3. User manages pets in dedicated screen
4. User goes back to Dashboard

### After (dialog-based):
1. User sees active pet card on Dashboard
2. User taps "Changer" button
3. Dialog overlays current screen showing all pets
4. User selects a pet or adds new one
5. Dialog closes, Dashboard updates with new active pet

## Benefits

✅ **Better UX:** Matches the reference mock app design  
✅ **Fewer Taps:** No need to switch tabs to change pets  
✅ **Context Preservation:** Dashboard remains visible behind dialog  
✅ **Cleaner Navigation:** 4 main tabs instead of 5  
✅ **Consistent Design:** Dialog follows Material 3 patterns

## Technical Details

### State Management:
```kotlin
val petsUiState by petsViewModel.uiState.collectAsState()
var showAnimalDialog by remember { mutableStateOf(false) }
val activePet = when (val state = petsUiState) {
    is PetsUiState.Success -> state.pets.firstOrNull()
    else -> null
}
```

### Dialog Architecture:
- Uses Compose `Dialog` composable
- Full-width, 90% height surface
- Rounded corners (16dp)
- Proper dismiss handling
- Callbacks for select and add actions

### Data Flow:
1. PetsViewModel provides `Flow<PetsUiState>`
2. Dashboard observes state as `State<PetsUiState>`
3. Active pet determined from first pet in list
4. Dialog displays all pets from state
5. Selection updates ViewModel (TODO: implement)

## Testing Performed

✅ App compiles without errors  
✅ Installs on Samsung Z Fold 6  
✅ Bottom navigation shows 4 tabs  
✅ Dashboard loads successfully  
✅ Active pet card displays (if pets exist)  
✅ "Changer" button functionality (opens dialog)  
✅ Dialog renders correctly  
✅ Dialog handles all state cases  

## Known TODO Items

1. **Set Active Pet:** Implement pet selection in ViewModel
2. **Calculate Rations:** Get daily food/water from ViewModel calculations
3. **Pet Details:** Implement "Détails" button navigation
4. **Add Pet:** Connect "+ Ajouter un animal" to AddPetDialog
5. **Persist Selection:** Save active pet preference locally

## Files Modified

1. `app/src/main/java/com/dobbleshop/neovision/ui/navigation/AppDestination.kt`
2. `app/src/main/java/com/dobbleshop/neovision/ui/DobbleShopApp.kt`
3. `app/src/main/java/com/dobbleshop/neovision/ui/navigation/AppNavHost.kt`
4. `app/src/main/java/com/dobbleshop/neovision/ui/screens/DashboardScreen.kt`

## References

- Mock App: https://3000-ipi4l7lcw7yz84frrk5d6-2e1b9533.sandbox.novita.ai/
- Screenshot: `animals-list-screen.png`
- Navigation follows Material 3 guidelines
- Dialog pattern matches mock implementation

---

**Date:** May 17, 2026  
**Build:** Debug APK, 21MB  
**Device:** Samsung Galaxy Z Fold 6 (RFCX70KWLNY)  
**Status:** ✅ Successfully deployed and tested
