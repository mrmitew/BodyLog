package com.github.mrmitew.bodylog.adapter.profile_common.interactor;

import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.domain.repository.Repository;
import com.github.mrmitew.bodylog.domain.repository.entity.Profile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoadProfileInteractorTest {
    @Mock
    Repository mMockRepository;
    LoadProfileInteractor mLoadProfileInteractor;

    public LoadProfileInteractorTest() {
    }

    @Before
    public void setUp() throws Exception {
        mLoadProfileInteractor =
                new LoadProfileInteractor(Runnable::run, Schedulers::trampoline, mMockRepository);
    }


    @Test
    public void shouldReturnInProgressState_WhenEmissionStarts() {
        //
        // Arrange
        //
        when(mLoadProfileInteractor.getUseCaseObservable())
                .thenReturn(Observable.just(mock(Profile.class)));

        //
        // Act
        //
        final TestObserver<LoadProfileInteractor.State> stateTestObserver =
                mLoadProfileInteractor.apply(Observable.just(new LoadProfileIntent())).test();

        final List<LoadProfileInteractor.State> values = stateTestObserver.values();

        //
        // Assert
        //
        final LoadProfileInteractor.State initialState = values.get(0);

        // First state should be "in progress"
        assertEquals(true, initialState.isInProgress());
        assertEquals(false, initialState.isSuccessful());
        assertEquals(StateError.Empty.Companion.getINSTANCE(), initialState.error());
        assertEquals(Profile.EMPTY, initialState.profile());

        // No errors should be emitted
        stateTestObserver.assertNoErrors();
    }

    @Test
    public void shouldReturnSuccessfulState_WhenProfileIsRetrieved() {
        //
        // Arrange
        //
        final Profile profile = Profile.builder()
                .name("Test")
                .description("Test")
                .weight(0)
                .bodyFat(0)
                .backSize(0)
                .chestSize(0)
                .armsSize(0)
                .waistSize(0)
                .build();

        when(mLoadProfileInteractor.getUseCaseObservable())
                .thenReturn(Observable.just(profile));

        //
        // Act
        //
        final TestObserver<LoadProfileInteractor.State> stateTestObserver =
                mLoadProfileInteractor.apply(Observable.just(new LoadProfileIntent())).test();

        final List<LoadProfileInteractor.State> values = stateTestObserver.values();

        //
        // Assert
        //
        assertEquals(2, values.size());

        final LoadProfileInteractor.State initialState = values.get(0);
        final LoadProfileInteractor.State secondState = values.get(1);

        // First state should be "in progress"
        assertEquals(true, initialState.isInProgress());
        assertEquals(false, initialState.isSuccessful());
        assertEquals(StateError.Empty.Companion.getINSTANCE(), initialState.error());
        assertEquals(Profile.EMPTY, initialState.profile());

        // Second state should be "successful"
        assertEquals(false, secondState.isInProgress());
        assertEquals(true, secondState.isSuccessful());
        assertEquals(StateError.Empty.Companion.getINSTANCE(), secondState.error());
        assertEquals(profile, secondState.profile());

        // No errors should be emitted
        stateTestObserver.assertNoErrors();
    }

    @Test
    public void shouldReturnMultipleSuccessfulStates_WhenMultipleProfilesAreEmitted() {
        //
        // Arrange
        //
        final Profile profile = Profile.builder()
                .name("Test")
                .description("Test")
                .weight(0)
                .bodyFat(0)
                .backSize(0)
                .chestSize(0)
                .armsSize(0)
                .waistSize(0)
                .build();

        when(mLoadProfileInteractor.getUseCaseObservable())
                .thenReturn(Observable.just(profile));

        // We'll emit two profiles to simulate profile update by the business logic, triggered
        // by another entity
        when(mMockRepository.getProfileRefreshing())
                .thenReturn(Observable.range(1, 2)
                        .flatMap(__ -> Observable.just(profile)));

        //
        // Act
        //
        final TestObserver<LoadProfileInteractor.State> stateTestObserver =
                mLoadProfileInteractor.apply(Observable.just(new LoadProfileIntent())).test();

        final List<LoadProfileInteractor.State> values = stateTestObserver.values();

        //
        // Assert
        //
        assertEquals(3, values.size());

        final LoadProfileInteractor.State initialState = values.get(0);
        final LoadProfileInteractor.State secondState = values.get(1);
        final LoadProfileInteractor.State thirdState = values.get(2);

        // First state should be "in progress"
        assertEquals(true, initialState.isInProgress());
        assertEquals(false, initialState.isSuccessful());
        assertEquals(StateError.Empty.Companion.getINSTANCE(), initialState.error());
        assertEquals(Profile.EMPTY, initialState.profile());

        // Second should be "successful"
        assertEquals(false, secondState.isInProgress());
        assertEquals(true, secondState.isSuccessful());
        assertEquals(StateError.Empty.Companion.getINSTANCE(), secondState.error());
        assertEquals(profile, secondState.profile());

        // Third should be also "successful"
        assertEquals(false, thirdState.isInProgress());
        assertEquals(true, thirdState.isSuccessful());
        assertEquals(StateError.Empty.Companion.getINSTANCE(), thirdState.error());
        assertEquals(profile, thirdState.profile());

        // No errors should be emitted
        stateTestObserver.assertNoErrors();
    }

    @Test
    public void shouldReturnErrorState_WhenAnExceptionIsThrown() {
        //
        // Arrange
        //
        final Throwable expectedError = new Throwable("Test");
        when(mLoadProfileInteractor.getUseCaseObservable())
                .thenReturn(Observable.error(expectedError));

        //
        // Act
        //
        final TestObserver<LoadProfileInteractor.State> stateTestObserver =
                mLoadProfileInteractor.apply(Observable.just(new LoadProfileIntent())).test();

        final List<LoadProfileInteractor.State> values = stateTestObserver.values();

        //
        // Assert
        //
        assertEquals(2, values.size());

        final LoadProfileInteractor.State secondState = values.get(1);

        // Second state should be "error"
        assertEquals(false, secondState.isInProgress());
        assertEquals(false, secondState.isSuccessful());
        assertEquals(expectedError, secondState.error());
        assertEquals(Profile.EMPTY, secondState.profile());

        // No errors should be emitted
        stateTestObserver.assertNoErrors();
    }
}