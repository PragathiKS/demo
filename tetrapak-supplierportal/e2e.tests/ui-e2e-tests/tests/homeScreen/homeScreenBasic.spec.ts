import { test, expect } from '@playwright/test';
import { HomeScreen } from '../../src/pages/home-screen/homescreen';

/**
 * @regression @sanity
 */
test('Verify the filters displayed in the Home Page', async ({ page }) => {
  const homeScreen = new HomeScreen(page);
  await homeScreen.goto();
})