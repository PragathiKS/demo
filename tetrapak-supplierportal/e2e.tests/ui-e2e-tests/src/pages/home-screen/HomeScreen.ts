import { expect, Locator, Page } from '@playwright/test';

export class HomeScreen {
  readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  async goto() {
    await this.page.goto('/');
  }
}