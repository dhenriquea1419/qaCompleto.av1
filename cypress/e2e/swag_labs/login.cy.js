/// <reference types="cypress" />

context('Testes de login válido e inválido', () => {
    beforeEach(() => {
        cy.visit('https://www.saucedemo.com/')
    });

    it('Login válido - Usuário Padrão', () => {
        cy.get('[data-test="username"]').type('standard_user');
        cy.get('[data-test="password"]').type('secret_sauce');
        cy.get('[data-test="login-button"]').click();
        cy.get('[data-test="title"]').should('be.visible');
    });
    
    it('Login inválido - Usuário inválido', () => {
        cy.get('[data-test="username"]').type('standard_errado');
        cy.get('[data-test="password"]').type('secret_sauce');
        cy.get('[data-test="login-button"]').click();
        cy.get('[data-test="error"]').should('have.text', 'Epic sadface: Username and password do not match any user in this service');
    });

    it('Login inválido - Senha inválida', () => {
         cy.get('[data-test="username"]').type('standard_user');
        cy.get('[data-test="password"]').type('secret_errad');
        cy.get('[data-test="login-button"]').click();
        cy.get('[data-test="error"]').should('have.text', 'Epic sadface: Username and password do not match any user in this service');
    });

    it('Login inválido - Username vazio', () => {
        cy.get('[data-test="username"]').clear();
        cy.get('[data-test="password"]').type('secret_errad');
        cy.get('[data-test="login-button"]').click();
        cy.get('[data-test="error"]').should('have.text', 'Epic sadface: Username is required');
    });

    it('Login inválido - Senha vazia', () => {
        cy.get('[data-test="username"]').clear().type('standard_user');
        cy.get('[data-test="password"]').clear();
        cy.get('[data-test="login-button"]').click();
        cy.get('[data-test="error"]').should('have.text', 'Epic sadface: Password is required');
    });

});