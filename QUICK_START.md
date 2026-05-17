# 🚀 Quick Start Guide - DOBBLESHOP NEO VISION

## Opening the Project in Android Studio

### Step 1: Launch Android Studio
1. Open Android Studio
2. Click **"Open"** from the welcome screen
3. Navigate to: `c:\Users\frtheola\Downloads\DOOBLESHOP\DOOBLESHOP\version Androi╠êd\dobbleshop-neo-vision-app`
4. Click **"OK"**

### Step 2: Wait for Gradle Sync
- Android Studio will automatically sync Gradle
- This takes 2-5 minutes on first run
- Watch the progress bar at the bottom
- Wait for "Gradle sync finished" message

### Step 3: Set Up an Emulator (if needed)
1. Click **Device Manager** (phone icon in toolbar)
2. Click **"Create Device"**
3. Select **"Pixel 6"** or any phone
4. Select **API 34** (Android 14)
5. Click **"Finish"**

### Step 4: Run the App
1. Make sure your emulator or device is selected in the toolbar
2. Click the green **▶ Run** button (or press Shift+F10)
3. Wait for app to build and install
4. App should launch automatically!

---

## ✅ What You Should See

### On Launch:
- Splash screen with DOBBLESHOP branding
- Main screen loads (Dashboard placeholder)
- Bottom navigation with 4 tabs:
  - 🏠 Accueil (Dashboard)
  - 🍽️ Repas (Feeding)
  - 📹 Caméra (Camera)
  - ⚙️ Réglages (Settings)

### When You Tap Tabs:
- Smooth transitions between screens
- Each screen shows:
  - Title bar
  - Emoji icon
  - "Coming in Phase X..." message

### Colors You Should See:
- **Primary Blue**: #1E6FFF (buttons, active tab)
- **Secondary Teal**: #00B3A4 (accent elements)
- **Background**: #F6F8FC (light gray)
- **White cards/surfaces**

---

## 🛠️ Development Workflow

### Using GitHub Copilot

#### For New Files:
1. Create new Kotlin file
2. Start typing class/function name
3. Press **Tab** to accept Copilot suggestions
4. Use **Cmd/Ctrl + I** for inline chat
5. Review and adjust generated code

#### For Composables:
```kotlin
// Type this comment, Copilot will suggest the composable:
// Create a Material 3 card with pet profile information
@Composable
fun PetProfileCard() {
    // Copilot will suggest implementation
}
```

#### For ViewModels:
```kotlin
// Copilot helps with ViewModel patterns:
@HiltViewModel
class DashboardViewModel @Inject constructor(
    // Copilot will suggest repository dependencies
) : ViewModel() {
    // Copilot suggests StateFlow patterns
}
```

### Testing Changes
1. Make code changes
2. Click **▶ Run** to rebuild
3. Or use **Ctrl+F9** to just build without running
4. Check for errors in **Build** output panel

### Debugging
1. Set breakpoints by clicking left margin
2. Click **🐛 Debug** instead of Run
3. Inspect variables in debugger panel
4. Use Logcat to see logs

---

## 📁 Where to Add New Code

### For Phase 2 (Data Layer):

#### Models:
```
app/src/main/java/com/dobbleshop/neovision/
└── domain/
    └── model/
        ├── PetProfile.kt         ← Create here
        ├── DeviceStatus.kt       ← Create here
        ├── ActivityEvent.kt      ← Create here
        └── FeedingSchedule.kt    ← Create here
```

#### Repositories:
```
app/src/main/java/com/dobbleshop/neovision/
└── data/
    └── repository/
        ├── PetRepository.kt      ← Interface
        └── FakePetRepository.kt  ← Mock implementation
```

#### Dependency Injection:
```
app/src/main/java/com/dobbleshop/neovision/
└── di/
    └── AppModule.kt              ← Hilt module
```

---

## 🎯 Phase 2 Checklist

Copy this into your task manager or notes:

```markdown
## Phase 2: Data Layer & Models

### Domain Models
- [ ] Create PetProfile.kt
  - id, name, species, breed, age, weight
  - dailyFoodGrams, dailyWaterMl
  
- [ ] Create DeviceStatus.kt
  - deviceId, variant, online, batteryPercent
  - foodReservoirGrams, waterReservoirPercent
  - bowlWeightGrams, firmwareVersion
  
- [ ] Create ActivityEvent.kt
  - id, type, icon, title, timestamp
  - hasSnapshot boolean
  
- [ ] Create FeedingSchedule.kt
  - id, petId, time, grams, daysOfWeek
  - isActive boolean

### Repository Interfaces
- [ ] Create PetRepository interface
  - getPets(): Flow<List<PetProfile>>
  - getPetById(id): PetProfile?
  - addPet(pet): Result<Unit>
  - updatePet(pet): Result<Unit>
  - deletePet(id): Result<Unit>
  
- [ ] Create DeviceRepository interface
  - getDeviceStatus(): Flow<DeviceStatus>
  - manualFeed(grams): Result<Unit>
  - manualWater(ml): Result<Unit>

### Mock Implementations
- [ ] Create FakePetRepository
  - Return demo pet "Léo" (Chat, 5.2kg, 4 years)
  - Support CRUD operations in memory
  
- [ ] Create FakeDeviceRepository
  - Return demo device status matching web demo
  - Simulate feed/water actions with delays
  
- [ ] Create FakeHistoryRepository
  - Return mock activity events

### Hilt Setup
- [ ] Create AppModule.kt
  - @Module and @InstallIn annotations
  - @Provides functions for repositories
  - Use @Singleton for shared instances
```

---

## 🐛 Common Issues & Solutions

### Issue: Gradle Sync Failed
**Solution**:
1. Check internet connection
2. File → Invalidate Caches → Restart
3. Try again after restart

### Issue: Cannot Resolve Symbol 'R'
**Solution**:
1. Build → Clean Project
2. Build → Rebuild Project
3. Wait for build to complete

### Issue: App Crashes on Launch
**Solution**:
1. Check Logcat for error message
2. Look for red stack trace
3. Find the line number and file
4. Check for typos or missing imports

### Issue: Compose Preview Not Working
**Solution**:
1. Make sure function has `@Preview` annotation
2. Click refresh icon in preview panel
3. Try Build → Make Project first

---

## 💡 Pro Tips

### Keyboard Shortcuts (Windows/Linux):
- **Ctrl + Space**: Auto-complete
- **Ctrl + /**: Comment/uncomment
- **Shift + F10**: Run app
- **Ctrl + F9**: Build without running
- **Ctrl + Shift + A**: Search actions
- **Alt + Enter**: Quick fix suggestions

### Copilot Tips:
- Write clear comments describing what you want
- Accept suggestions with **Tab**
- Reject with **Esc**
- Cycle through suggestions with **Alt + ]**
- Use chat for complex logic

### Compose Tips:
- Use `@Preview` to see composables instantly
- Extract functions: **Ctrl + Alt + M**
- Generate imports: **Alt + Enter**
- Use Material 3 components by default

---

## 📚 Resources

### Official Docs:
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material 3](https://m3.material.io/)
- [Hilt DI](https://dagger.dev/hilt/)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

### Project Files:
- 📄 **DEVELOPMENT_PLAN.md**: Full 8-10 week roadmap
- 📄 **PHASE_1_COMPLETE.md**: What we just completed
- 📄 **README.md**: Project overview

---

## 🎉 Ready to Code!

You now have:
- ✅ Working Android project
- ✅ All dependencies configured
- ✅ Navigation working
- ✅ Design system ready
- ✅ Clear roadmap for next steps

**Start with Phase 2** and follow the checklist above.

**Happy coding! 🐾**

---

**Need help?** Refer to the main DEVELOPMENT_PLAN.md or create detailed comments for Copilot to assist you.
